# Generated by Django 2.1.3 on 2018-11-21 21:25

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('learn', '0002_challenge_desc'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='challenge',
            name='desc',
        ),
        migrations.AlterField(
            model_name='challenge',
            name='path',
            field=models.FilePathField(),
        ),
    ]
